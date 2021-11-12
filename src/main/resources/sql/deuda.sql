SELECT (c.saldo_actual + c.saldo_int_acum + c.saldo_imp_no_suj_i + c.saldo_imp_suj_int - c.valor_anticipo) deuda_total,
       CASE
          WHEN h.tipo_iva = 'RIN' THEN ('A' || h.centro_emisor || h.tipo_docto || lpad(h.numero_factura, 8, '0') || '17')
          ELSE 'B' || h.centro_emisor || tipo_docto || lpad(h.numero_factura, 8, '0') || '18'
       END numero_factura,
       h.total_a_pagar importe,
       nvl(ht.tasa_facturada + ht.saldo_ant_tasa, 0) deuda_alumbrado_publico,
       nvl(monto_disputa, 0) saldo_disputa,
       (SELECT (today - h2.fecha_vencimiento1)
          FROM hisfac h2
         WHERE h2.numero_cliente = c.numero_cliente
           AND h2.corr_facturacion = (c.corr_facturacion - c.antiguedad_saldo)
           AND c.antiguedad_saldo > 0) antiguedad
  FROM cliente c
       JOIN hisfac h ON (h.numero_cliente = c.numero_cliente AND h.corr_facturacion = c.corr_facturacion)
       LEFT OUTER JOIN hisfac_tasa ht ON (ht.numero_cliente = c.numero_cliente AND ht.corr_facturacion = c.corr_facturacion)
       LEFT OUTER JOIN sd_saldo_disputa sd ON (sd.numero_cliente = c.numero_cliente AND sd.estado = 'V')
 WHERE c.numero_cliente = :#${header.numeroCliente}

