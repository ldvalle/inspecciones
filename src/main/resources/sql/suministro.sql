SELECT c.numero_cliente numero_suministro,
       c.fecha_a_corte fecha_corte,
       c.estado_suministro,
       c.estado_cliente estado_conexion,
       c.tiene_cnr toi,
       decode(c.estado_facturacion, 0, 'N', 'S') periodo_facturacion,
       trim(t.descripcion) estado_cobrabilidad
  FROM cliente c
       JOIN tabla t ON (t.codigo = c.estado_cobrabilida AND t.nomtabla = 'ESTCOB' AND t.sucursal = '0000')
 WHERE c.numero_cliente = :#${header.numeroCliente}