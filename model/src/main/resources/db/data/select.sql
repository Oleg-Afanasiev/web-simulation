-- SELECT ALL: arc(s)
SELECT arc.arc_id, stLeft.name AS left, stRight.name AS right, duration
FROM  arc, station AS stLeft, station AS stRight
WHERE arc.node_left_id = stLeft.station_id AND arc.node_right_id = stRight.station_id;

-- SELECT ALL: route(s)
SELECT r.route_id, s.name, r.number, r.describe,
--   CASE  WHEN r.type_circ=FALSE THEN 'Simple'
--         ELSE 'Circular'
--   END AS type,
--
--   CASE  WHEN r.direct_forw=TRUE THEN 'Forward'
--         ELSE 'Back'
--   END AS direct,
  r.cost
FROM route AS r, station AS s
WHERE s.station_id = r.first_node_id;

-- SELECT ALL: route_arc(s)
SELECT r.route_id, r.number,
--   CASE  WHEN r.direct_forw=TRUE THEN 'Forward'
--         ELSE 'Back'
--   END AS direct,
  sl.name AS left, sr.name AS right
FROM route AS r, route_arc AS ra, arc AS a, station AS sl, station AS sr
WHERE r.route_id = ra.route_id
AND ra.arc_id = a.arc_id AND a.node_left_id = sl.station_id AND a.node_right_id = sr.station_id;

