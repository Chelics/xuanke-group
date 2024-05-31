# SElectU Course Selection Management System

## Introduction

**SElectU**, a blend of **SEU**—the acronym for Southeast University—and the word **select**, is an innovative Web-based course selection system. It not only provides basic course selection features but also brings an unprecedented convenient experience to users through efficient parallel processing and automatic scheduling technologies.

## Software Architecture

The system adopts a front-end and back-end separation model, divided into three functional modules: students, teachers, and academic affairs.

- **Back-end**: Utilizes **SpringBoot v3.2.5**, following the MVC architectural pattern.
- **Front-end**: Based on **Vue3**, it provides an intuitive user interface.

## User Instructions

Depending on the identity of the user logging in, different operations can be performed after logging in:

- **Students** (username starting with `0`): View class schedules, enroll in or drop courses, and perform fuzzy searches for courses.
- **Teachers** (username starting with `1`): View class schedules, declare courses, and view declared course information.
- **Academic Affairs** (username starting with `2`): Edit the names and times of course selection phases, review courses, and view classroom schedules.

## Features

1. **Secure Login**: Identity verification and authorization using **JWT tokens**.
2. **Peak Time Handling**: Utilizes the **CompletableFuture** class and a simple message queue to asynchronously process course selection requests in parallel through a thread pool.
3. **Automatic Scheduling**: For commonly used class hours, the system can automatically allocate time and classrooms for courses.

## How to Contribute

We welcome contributions from community members! If you are interested in helping to improve SElectU, please check out our contribution guide.

## Issue Tracking

Encountering issues or have feature suggestions? Please let us know in our issue tracker.