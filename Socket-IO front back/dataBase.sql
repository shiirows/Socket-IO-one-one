CREATE DATABASE projetcar;
USE projetcar;

CREATE TABLE Agences (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    adress VARCHAR(100) NOT NULL,
    country VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL
);

CREATE TABLE Users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    password VARCHAR(220) NOT NULL,
    dateOfBirth DATE,
    adress VARCHAR(100) NOT NULL,
    countryCode VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL
);

CREATE TABLE Vehicules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    categorie VARCHAR(50) NOT NULL,
    marque VARCHAR(50) NOT NULL,
    modele VARCHAR(50) NOT NULL,
    price DECIMAL(15, 2)
);

CREATE TABLE Reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    vehicule_id BIGINT,
    agence_depart_id BIGINT,
    agence_retour_id BIGINT,
    date_debut DATE,
    date_fin DATE,
    statut VARCHAR(50),
    price DECIMAL(15, 2),
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (vehicule_id) REFERENCES Vehicules(id),
    FOREIGN KEY (agence_depart_id) REFERENCES Agences(id),
    FOREIGN KEY (agence_retour_id) REFERENCES Agences(id)
);

CREATE TABLE Paiements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    price DECIMAL(15, 2),
    reservation_id BIGINT,
    date_paiement DATE,
    statut VARCHAR(50),
    FOREIGN KEY (reservation_id) REFERENCES Reservations(id)
);

CREATE TABLE Messages_Form (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_sender_id BIGINT,
    message TEXT NOT NULL,
    created_at DATE,
    statut BOOLEAN NOT NULL,
    sujet VARCHAR(100) NOT NULL,
    FOREIGN KEY (user_sender_id) REFERENCES Users(id)
);

CREATE TABLE Messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_sender_id BIGINT,
    user_receiver_id BIGINT,
    message TEXT NOT NULL,
    created_at DATE,
    statut BOOLEAN NOT NULL,
    FOREIGN KEY (user_sender_id) REFERENCES Users(id),
    FOREIGN KEY (user_receiver_id) REFERENCES Users(id)
);
