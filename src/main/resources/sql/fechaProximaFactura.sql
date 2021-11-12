SELECT 'fechaProximaFactura' property,
       min(fecha_emision) valor
  FROM agenda a
       JOIN cliente c ON (a.sucursal = c.sucursal AND a.sector = c.sector)
 WHERE a.estado = 1
   AND a.fecha_generacion >= today
   AND c.numero_cliente = :#${header.numeroCliente}
