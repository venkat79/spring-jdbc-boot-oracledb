  CREATE TABLE rack (
  id NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
  name varchar2(50) NOT NULL,
  rack_type varchar2(50) NOT NULL,
  CONSTRAINT rack_pk PRIMARY KEY (id)
  );

  CREATE TABLE compute_node (
  id NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
  name varchar2(50) NOT NULL,
  exadata_rack_id NUMBER,
  CONSTRAINT compute_node_pk PRIMARY KEY(id),
  CONSTRAINT compute_node_foreign_key_rack FOREIGN KEY (exadata_rack_id) REFERENCES rack(id)
  );

  CREATE TABLE rack_slot (
  id NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
  name varchar2(50) NOT NULL,
  exadata_rack_id NUMBER,
  CONSTRAINT rack_slot_pk PRIMARY KEY(id)
  );

  CREATE TABLE virtual_machine(
   id NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
   name varchar2(50) NOT NULL,
   rack_slot_id NUMBER,
   compute_node_id NUMBER,
   CONSTRAINT virtual_machine_foreign_key_rack_slot FOREIGN KEY (rack_slot_id) REFERENCES rack_slot(id),
   CONSTRAINT virtual_machine_foreign_key_compute_node FOREIGN KEY (compute_node_id) REFERENCES compute_node(id)
  );





