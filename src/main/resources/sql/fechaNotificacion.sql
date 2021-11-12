SELECT 'fechaNotificacion' property,
       max(hc.fecha_generacion) valor
  FROM cliente c
       JOIN hisfac hf ON (hf.numero_cliente = c.numero_cliente AND hf.corr_facturacion = c.corr_facturacion)
       JOIN hisnotcor hc ON (hc.numero_cliente = c.numero_cliente)
 WHERE hc.tipo_accion = 'N'
   AND date(hc.fecha_generacion) >= hf.fecha_facturacion
   AND c.numero_cliente = :#${header.numeroCliente}