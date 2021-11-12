SELECT corr_facturacion corrFactu,
fecha_lectura fechaLectu,
lectura_terreno lectura,
tipo_lectura tipoLectu,
numero_medidor nroMedidor,
marca_medidor marcaMedidor
FROM hislec
WHERE numero_cliente =  :#${header.numeroCliente}
ORDER BY corr_facturacion
