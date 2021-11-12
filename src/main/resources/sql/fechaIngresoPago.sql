SELECT 'fechaIngresoPago' property,
       max(fecha_actualiza) valor
  FROM pagco
 WHERE numero_cliente = :#${header.numeroCliente}
   AND fecha_actualiza IS NULL
   AND cajero NOT IN ('0000', '8888')
   AND oficina NOT IN ('0000', '9990')
