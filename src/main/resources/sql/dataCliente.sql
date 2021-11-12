SELECT c.estado_cliente stsCliente, 
c.estado_facturacion stsFacturacion,
c.tipo_fpago tipoFpago,
c.tipo_reparto tipoReparto,
DECODE(v.numero_cliente, NULL, 'N', 'S') esElectro
FROM cliente c, OUTER( clientes_vip v, tabla t1)
WHERE c.numero_cliente = :#${header.numeroCliente}
AND v.numero_cliente = c.numero_cliente
AND v.fecha_activacion <= TODAY 
AND (v.fecha_desactivac IS NULL OR v.fecha_desactivac > TODAY) 
AND t1.nomtabla = 'SDCLIV' 
AND t1.codigo = v.motivo 
AND t1.valor_alf[4] = 'S' 
AND t1.sucursal = '0000' 
AND t1.fecha_activacion <= TODAY 
AND ( t1.fecha_desactivac >= TODAY OR t1.fecha_desactivac IS NULL )
