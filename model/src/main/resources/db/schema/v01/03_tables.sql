set schema 'bts';

-- tables
-- Table: station
CREATE TABLE station (
  station_id bigserial NOT NULL,
  name varchar NOT NULL,
  CONSTRAINT station_pk PRIMARY KEY(station_id)
);

-- Table: arc
CREATE TABLE arc (
  arc_id bigserial NOT NULL,
  node_left_id bigint NOT NULL,
  node_right_id bigint NOT NULL,
  duration int NOT NULL CHECK (duration > 0),
  CHECK (node_left_id != node_right_id),
  CONSTRAINT arc_pk PRIMARY KEY (arc_id)
);

-- Table: route
CREATE TABLE route (
  route_id bigserial NOT NULL,
  first_node_id bigint NOT NULL,
  number varchar(30) NOT NULL,
  describe varchar,
  is_deleted boolean  DEFAULT FALSE,
  --type_circ boolean DEFAULT FALSE,
  --direct_forw boolean NOT NULL,
  cost numeric NOT NULL,
  CONSTRAINT route_pk PRIMARY KEY (route_id)
);

-- Table: route_arc
CREATE TABLE route_arc (
  route_id bigint NOT NULL,
  arc_id bigint NOT NULL,
  CONSTRAINT route_arc_pk PRIMARY KEY (route_id, arc_id)
);

-- Table: route_pair
CREATE TABLE route_pair (
  route_forw_id bigint NOT NULL,
  route_back_id bigint NOT NULL,
  CHECK (route_forw_id != route_back_id),
  CONSTRAINT route_pair_pk PRIMARY KEY (route_forw_id, route_back_id)
);

-- Table: map
CREATE TABLE map (
  map_id bigserial NOT NULL,
  name varchar NOT NULL,
  describe varchar,
  CONSTRAINT map_pk PRIMARY KEY (map_id)
);

-- Table: map_route:
CREATE TABLE map_route (
  map_id bigint NOT NULL,
  route_id bigint NOT NULL,
  CONSTRAINT map_route_pk PRIMARY KEY (map_id, route_id)
);

-- Table: bus
CREATE TABLE bus (
  bus_id bigserial NOT NULL,
  number varchar(30) NOT NULL,
  capacity int NOT NULL CHECK(capacity > 0),
  CONSTRAINT bus_pk PRIMARY KEY (bus_id)
);

-- Table: user
CREATE TABLE "user" (
  user_id bigserial NOT NULL,
  username varchar(30) NOT NULL,
  password varchar(30) NOT NULL,
  first_name varchar(30) NOT NULL,
  last_name varchar(30) NOT NULL,
  email varchar(30) NOT NULL,
  created timestamp  DEFAULT now(),
  updated timestamp  DEFAULT now(),
  is_deleted boolean DEFAULT FALSE,
  CONSTRAINT user_pk PRIMARY KEY (user_id)
);

-- foreign keys
-- Reference: arc_node_left (table: arc)
ALTER TABLE arc ADD CONSTRAINT arc_node_left
  FOREIGN KEY (node_left_id)
  REFERENCES station (station_id)
  NOT DEFERRABLE
  INITIALLY IMMEDIATE
  ;

-- Reference: arc_node_right (table: arc)
ALTER TABLE arc ADD CONSTRAINT arc_node_right
  FOREIGN KEY (node_right_id)
  REFERENCES station (station_id)
  NOT DEFERRABLE
  INITIALLY IMMEDIATE
  ;

-- Reference: route_station (table: route)
ALTER TABLE route ADD CONSTRAINT route_station
  FOREIGN KEY (first_node_id)
  REFERENCES station (station_id)
  NOT DEFERRABLE
  INITIALLY IMMEDIATE
  ;

-- Reference: route_arc_to_arc (table: route_arc)
ALTER TABLE route_arc ADD CONSTRAINT route_arc_to_arc
  FOREIGN KEY (arc_id)
  REFERENCES arc (arc_id)
  NOT DEFERRABLE
  INITIALLY IMMEDIATE
  ;

-- Reference: route_arc_to_route (table: route_arc)
ALTER TABLE route_arc ADD CONSTRAINT route_arc_to_route
  FOREIGN KEY (route_id)
  REFERENCES route (route_id)
  NOT DEFERRABLE
  INITIALLY IMMEDIATE
  ;

-- Reference: route_pair_to_route_forward (table: route_pair)
ALTER TABLE route_pair ADD CONSTRAINT route_pair_to_route_forward
  FOREIGN KEY (route_forw_id)
  REFERENCES route(route_id)
  NOT DEFERRABLE
  INITIALLY IMMEDIATE
  ;

-- Reference: route_pair_to_route_back (table: route_pair)
ALTER TABLE route_pair ADD CONSTRAINT route_pair_to_route_back
  FOREIGN KEY (route_back_id)
  REFERENCES route(route_id)
  NOT DEFERRABLE
  INITIALLY IMMEDIATE
  ;

-- Reference: map_route_to_route (table: map_route)
ALTER TABLE map_route ADD CONSTRAINT map_route_to_route
  FOREIGN KEY (route_id)
  REFERENCES route (route_id)
  NOT DEFERRABLE
  INITIALLY IMMEDIATE
  ;

-- Reference: map_route_to_map (table: map_route)
ALTER TABLE map_route ADD CONSTRAINT map_route_to_map
  FOREIGN KEY (map_id)
  REFERENCES map (map_id)
  NOT DEFERRABLE
  INITIALLY IMMEDIATE
  ;

-- End of file.