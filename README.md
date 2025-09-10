# Template Configurator
Application used as a tool for scanning and changing ODT files in specified folder and it's subfolders

## Features

- Scan ODT template files for `[import ...]` placeholders
- Generates JSON file with list of template files and import files relations
- Using JSON configuration file detects specified imports and replaces them with new ones
- Runnable as a standalone Spring Boot JAR (no Maven required on the client machine)

## Requirements
- Java 17+ installed
- Internet connection for downloading dependencies

## Getting started
1. Clone the repository
2. Setting `application.properties`:
    - Location of ODT templates and files used in templates:
      - `template.configuration.files-location-path`
    - location of JSON file used for template import link/dependencies changing
      - `template.configuration.odt-configuration-json-path=src\\main\\resources\\block_replacement_configuration.json`
    -  location of JSON containing template - block files links
      - `template.configuration.links-output-path`
   - log file location
     - `logging.file.name`
3. Generate application .jar running `./mvnw clean package`
4. Run java project `java -jar target\templateconfigurator-0.1.jar`

## Libraries used in project

- **Spring Boot** – for building and running the application
- **Jackson** – for JSON serialization/deserialization
- **ODF Toolkit (odftoolkit)** – for reading/writing ODT documents
- **Lombok** - to reduce boilerplate code and easier logging