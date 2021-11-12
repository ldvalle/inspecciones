SELECT 'cortePorDeuda' property,
       CASE
       	 WHEN cant = 0 THEN 'N'
         ELSE 'S'
       END valor
  FROM (
    SELECT count(*) cant
      FROM cliente c
           JOIN correp p ON (p.numero_cliente = c.numero_cliente AND p.corr_corte = c.corr_corte)
     WHERE c.numero_cliente = :#${header.numeroCliente}
       AND c.estado_suministro = 1
       AND p.motivo_corte = '01'
    )

