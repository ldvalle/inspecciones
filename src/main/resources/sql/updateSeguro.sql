UPDATE clientes_seguros SET
fecha_baja = :#${header.fechaBaja},
rol_baja = :#${header.rolBaja}
WHERE numero_cliente = :#${header.numeroCliente}
AND cod_empresa = :#${header.codEmpresa}
AND concepto = :#${header.concepto}
AND fecha_baja IS NULL
