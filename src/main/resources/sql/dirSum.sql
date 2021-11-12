SELECT c.cod_calle codCalle,
TRIM(c.nom_calle) nomCalle,
TRIM(c.nro_dir) altura,
TRIM(c.piso_dir) piso,
TRIM(c.depto_dir) depto,
TRIM(c.nom_partido) partido,
TRIM(c.nom_comuna) localidad,
TRIM(c.nombre) nombreCliente,
c.tiene_corte_rest corteRestringido,
c.tipo_fpago tipoFpago,
CASE
		WHEN (select count(*) from clientes_vip v
				where v.numero_cliente = c.numero_cliente
				and v.fecha_activacion <= today
				and (v.fecha_desactivac is null or v.fecha_desactivac > today))>0 then 'S'
	ELSE 'N'
END esElectro,
' ' motivosRestringido
FROM cliente c
WHERE c.numero_cliente = :#${header.numeroCliente}


