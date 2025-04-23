
CREATE TABLE ciudades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    fecha DATE NOT NULL,
    dia_semana VARCHAR(20),
    UNIQUE KEY unique_nombre_fecha (nombre, fecha)
);

create table deportes(

    id bigint not null auto_increment,
    nombre varchar(100) not null unique,
    icono varchar(20) not null unique,

    primary key(id)
);

CREATE TABLE datos_climaticos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ciudad_id BIGINT NOT NULL,
    agv_temperatura_diaria DOUBLE,
    temperatura_max DOUBLE,
    temperatura_min DOUBLE,
    agv_velocidad_viento DOUBLE,
    agv_humedad DOUBLE,
    estado VARCHAR(255),
    descripcion VARCHAR(255),
    icono VARCHAR(255),
    deporte_id BIGINT,
    porcentaje_idoneidad INT,  
    
    CONSTRAINT fk_ciudad FOREIGN KEY (ciudad_id) REFERENCES ciudades(id) ON DELETE CASCADE,
    CONSTRAINT fk_deporte FOREIGN KEY (deporte_id) REFERENCES deportes(id)
);
