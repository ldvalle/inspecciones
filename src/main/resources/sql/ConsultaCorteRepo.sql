SELECT c1.numero_cliente numerosuministro,
' ' nroOrdenSap,
(c1.numero_cliente || LPAD(c1.corr_corte, 4, '0') || 'CORTFOPARG') idCorteRepo,
'C' tipoRegistro,
sm1.cod_sf1 motivo,
CASE
	WHEN c1.fecha_corte IS NOT NULL AND c1.accion_corte IN ( '16', '34', '35', '30', '40', '41') THEN '99'
	WHEN c1.fecha_corte IS NULL THEN '20'
  	ELSE '21'
END estado,
c1.fecha_corte fechaEjecucion,
c1.fecha_ini_evento fechaSolicitud,
sa1.cod_sf1 accionRealizada,
' ' usuario,
' ' tipo
FROM correp c1, sf_transforma sm1, OUTER (sf_transforma sa1)
WHERE c1.numero_cliente = :#${header.numeroSuministro}
AND c1.fecha_ini_evento BETWEEN :#${header.fechaDesde} AND :#${header.fechaHasta}
AND c1.motivo_corte IS NOT NULL
AND c1.tramite_repo IS NULL
AND sm1.clave = 'MOTCORTE'
AND sm1.cod_mac = c1.motivo_corte
AND sa1.clave = 'ACCORTE'
AND sa1.cod_mac = c1.accion_corte
UNION
SELECT c2.numero_cliente numerosuministro,
' ' nroOrdenSap,
(c2.numero_cliente || LPAD(c2.corr_repo, 4, '0') || 'REPOFOPARG') idCorteRepo,
'R' tipoRegistro,
sm2.cod_sf1 motivo,
CASE
	WHEN c2.fecha_sol_repo IS NULL THEN '22'
  ELSE '30'
END estado,
c2.fecha_reposicion fechaEjecucion,
c2.fecha_ini_evento fechaSolicitud,
sa2.cod_sf1 accionRealizada,
' ' usuario,
' ' tipo
FROM correp c2, sf_transforma sm2, OUTER (sf_transforma sa2)
WHERE c2.numero_cliente = :#${header.numeroSuministro}
AND c2.fecha_sol_repo BETWEEN :#${header.fechaDesde} AND :#${header.fechaHasta}
AND c2.motivo_corte IS NOT NULL
AND sm2.clave = 'MOTREPO'
AND sm2.cod_mac = c2.motivo_repo
AND sa2.clave = 'ACREPO'
AND sa2.cod_mac = c2.accion_rehab
UNION
SELECT c3.numero_cliente numerosuministro,
' ' nroOrdenSap,
c3.numero_cliente || TO_CHAR(c3.fecha_solicitud, '%Y%m%d') || 'PRORFOPARG' idCorteRepo,
'P' tipoRegistro,
sm3.cod_sf1 motivo,
CASE
	WHEN c3.fecha_anterior + c3.dias > TODAY THEN 'Approved'
  ELSE 'Cancelled'
END estado,
c3.fecha_solicitud fechaEjecucion,
c3.fecha_solicitud fechaSolicitud,
' ' accionRealizada,
c3.rol usuario,
c3.tipo tipo
FROM corplazo c3, sf_transforma sm3
WHERE c3.numero_cliente = :#${header.numeroSuministro}
AND date(c3.fecha_solicitud) BETWEEN :#${header.fechaDesde} AND :#${header.fechaHasta}
AND sm3.clave= 'MOTPRORO'
AND sm3.cod_mac = c3.cod_motivo
UNION
select c4.numero_cliente numerosuministro,
' ' nroOrdenSap,
(c4.numero_cliente || LPAD(c4.corr_corte, 4, '0') || 'CORTFOPARG') idCorteRepo,
'C' tipoRegistro,
sm1.cod_sf1 motivo,
'20' estado,
c4.fecha_generacion fechaEjecucion,
c4.fecha_solicitud fechaSolicitud,
' ' accionRealizada,
' ' usuario,
' ' tipo
from corsoco c4, sf_transforma sm1
where c4.numero_cliente = :#${header.numeroSuministro}
and date(c4.fecha_solicitud) BETWEEN :#${header.fechaDesde} AND :#${header.fechaHasta}
and c4.estado in('G', 'S')
AND sm1.clave = 'MOTCORTE'
AND sm1.cod_mac = c4.motivo_sol
UNION
select c5.numero_cliente numerosuministro,
' ' nroOrdenSap,
(c5.numero_cliente || LPAD(c5.corr_repo, 4, '0') || 'CORTFOPARG') idCorteRepo,
'R' tipoRegistro,
sm1.cod_sf1 motivo,
'22' estado,
c5.fecha_generacion fechaEjecucion,
c5.fecha_solicitud fechaSolicitud,
' ' accionRealizada,
' ' usuario,
' ' tipo
from corsore c5, sf_transforma sm1
where c5.numero_cliente = :#${header.numeroSuministro}
and date(c5.fecha_solicitud) BETWEEN :#${header.fechaDesde} AND :#${header.fechaHasta}
and c5.estado in('G', 'S')
AND sm1.clave = 'MOTREPO'
AND sm1.cod_mac = c5.motivo_sol
ORDER BY 8
