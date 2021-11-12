INSERT INTO arrie (
numero_cliente,
codigo_cargo,
valor_cargo,
factor_aplicacion,
fecha_activacion
)VALUES(
:#${header.numeroCliente},
:#${header.concepto},
:#${header.monto},
1,
:#${header.fechaAlta})
