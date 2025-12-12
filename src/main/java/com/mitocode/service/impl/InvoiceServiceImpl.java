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

                // ============================
                // 1. Cargar CLIENT completo
                // ============================
                .flatMap(inv ->
                        clientRepo.findById(inv.getClient().getId())
                                .map(client -> {
                                    inv.setClient(client);
                                    return inv;
                                })
                )

                // ============================
                // 2. Cargar cada DISH completo
                // ============================
                .flatMap(inv ->
                        Flux.fromIterable(inv.getItems())
                                .flatMap(item ->
                                        dishRepo.findById(item.getDish().getId())
                                                .map(fullDish -> {
                                                    item.setDish(fullDish);  // Dish lleno
                                                    return item;
                                                })
                                )
                                .collectList()
                                .map(itemsList -> {
                                    inv.setItems(itemsList);
                                    return inv;
                                })
                )

                // ============================
                // 3. GENERAR PDF CON JASPER
                // ============================
                .map(inv -> {

                    try {
                        Map<String, Object> parameters = new HashMap<>();
                        parameters.put("txt_client", inv.getClient().getFirstName());

                        InputStream stream = getClass()
                                .getResourceAsStream("/facturas.jrxml");

                        JasperReport report = JasperCompileManager.compileReport(stream);

                        JRBeanArrayDataSource dataSource =
                                new JRBeanArrayDataSource(inv.getItems().toArray());

                        JasperPrint print =
                                JasperFillManager.fillReport(report, parameters, dataSource);

                        return JasperExportManager.exportReportToPdf(print);

                    } catch (JRException e) {
                        e.printStackTrace();
                        return new byte[0];
                    }

                });
    }


}
