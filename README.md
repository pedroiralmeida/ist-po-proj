# PRR Management Application

## Overview

The PRR Management Application is designed to manage a network of communication terminals. This application facilitates the registration, management, and querying of customers, terminals, and communications. It provides robust functionality for handling various aspects of communication networks, including customer management, terminal operations, communication tracking, and billing.

## Functionality

### Customer Management
- **Registration and Management**: Register new customers with unique identifiers, and manage their details including name and tax identification number.
- **Customer Types**: Support for different customer types (Normal, Gold, Platinum) with rules for upgrading and downgrading based on usage and balance criteria.
- **Balance Tracking**: Maintain and calculate the balance for each customer, considering payments made and outstanding debts.

### Terminal Management
- **Terminal Registration**: Register new communication terminals with unique identifiers.
- **Terminal Association**: Associate terminals with customers for management and billing purposes.

### Communication Management
- **Communication Tracking**: Record details of communications made through the terminals, including the type of communication and duration.
- **Billing and Invoicing**: Calculate costs associated with communications based on customer type and communication plans.

### Notification System
- **Event Notifications**: Enable customers to receive notifications about events related to their terminals. Notifications can be delivered through various means such as postal mail, SMS, email, or simply logged in the application.
- **Notification Management**: Customers can activate or deactivate notifications at any time.

### Data Management
- **Persistent Storage**: Save and restore the current state of the application, preserving all relevant information.
- **Textual Database**: Load a predefined textual database at the start of the application to initialize concepts and entities.

### Search and Query
- **Advanced Search**: Perform searches based on various criteria across different managed entities, including customers, terminals, and communications.

### Design Requirements
- **Extensibility**: The application design supports easy extensions or modifications with minimal impact on existing code. This includes adding new customer types, communication methods, pricing plans, and search capabilities.
- **User Interaction**: Commands in the application prompt for all necessary information before validation, and user interactions are facilitated through defined forms and displays.

### Implementation Notes
- **Predefined Classes**: The core functionality of the application is partially implemented in predefined classes such as `prr.Manager`. These should be adapted and extended as needed.
- **No Multiple States**: The application does not support maintaining multiple versions of its state simultaneously.

## Project Documentation

For detailed information on the project, please refer to the [full project statement](https://web.tecnico.ulisboa.pt/~david.matos/w/pt/index.php/Programa%C3%A7%C3%A3o_com_Objectos/Projecto_de_Programa%C3%A7%C3%A3o_com_Objectos/Enunciado_do_Projecto_de_2022-2023).

