SELECT 'pagoEnProceso' property,
       CASE
        WHEN cant = 0 THEN 'N'
        ELSE 'S'
       END valor
  FROM (
    SELECT count(*) cant
      FROM pagco
     WHERE numero_cliente = :#${header.numeroCliente}
       AND fecha_actualiza is null
       AND cajero NOT IN ('0000', '8888')
       AND oficina NOT IN ('0000', '9990')
    )
