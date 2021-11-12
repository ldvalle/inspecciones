INSERT INTO mi_cliente (
numero_cliente,
nro_orden,
nombre,
tipo_doc,
nro_doc,
telefono
)VALUES(
:#${header.numeroCliente},
:#${header.nroOrden},
:#${header.nombreCliente},
:#${header.tipoDocumento},
:#${header.nroDocumento},
:#${header.telefono})
