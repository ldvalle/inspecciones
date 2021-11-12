UPDATE arrie SET
fecha_desactivac = :#${header.fechaBaja}
WHERE numero_cliente = :#${header.numeroCliente}
AND codigo_cargo = :#${header.concepto}
AND fecha_desactivac IS NULL
