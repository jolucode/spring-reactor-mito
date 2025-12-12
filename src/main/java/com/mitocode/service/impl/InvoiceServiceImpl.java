package com.mitocode.service.impl;

import com.mitocode.model.Invoice;
import com.mitocode.model.InvoiceDetail;
import com.mitocode.repo.IClientRepo;
import com.mitocode.repo.IDishRepo;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IInvoiceRepo;
import com.mitocode.service.IInvoiceService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl extends CRUDImpl<Invoice, String> implements IInvoiceService {

    private final IInvoiceRepo repo;
    private final IDishRepo dishRepo;
    private final IClientRepo clientRepo;

    @Override
    protected IGenericRepo<Invoice, String> getRepo() {
        return repo;
    }


    @Override
    public Mono<byte[]> generarReport(String idInvoice) {
        return repo.findById(idInvoice)
                .flatMap(this::populateClient)
                .flatMap(this::populateDishes)
                .map(this::generatePdfReport);
    }

    private Mono<Invoice> populateClient(Invoice invoice) {
        return clientRepo.findById(invoice.getClient().getId())
                .map(client -> {
                    invoice.setClient(client);
                    return invoice;
                });
    }

    private Mono<Invoice> populateDishes(Invoice invoice) {
        return Flux.fromIterable(invoice.getItems())
                .flatMap(item ->
                        dishRepo.findById(item.getDish().getId())
                                .map(fullDish -> {
                                    item.setDish(fullDish);  // Dish lleno
                                    return item;
                                })
                )
                .collectList()
                .map(itemsList -> {
                    invoice.setItems(itemsList);
                    return invoice;
                });
    }

    private byte[] generatePdfReport(Invoice invoice) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("txt_client", invoice.getClient().getFirstName());
            InputStream stream = getClass().getResourceAsStream("/facturas.jrxml");
            JasperReport report = JasperCompileManager.compileReport(stream);
            JRBeanArrayDataSource dataSource = new JRBeanArrayDataSource(invoice.getItems().toArray());
            JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);
            return JasperExportManager.exportReportToPdf(print);

        } catch (JRException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }


}
