# 🗳️ Secure Electronic Voting System

A comprehensive console-based secure voting system demonstrating cryptographic principles and election security protocols.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Security Properties](#security-properties)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Project Structure](#project-structure)
- [Usage](#usage)
- [Security Implementation](#security-implementation)
- [Sample Output](#sample-output)
- [Development Timeline](#development-timeline)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

This project implements a secure electronic voting system that demonstrates four fundamental security properties required in digital elections:

- **🔒 Integrity** - Protection against vote tampering
- **👤 Anonymity** - Voter privacy protection
- **🤐 Confidentiality** - Vote secrecy through encryption
- **✅ Authentication** - Fraud prevention and voter verification

## ✨ Features

### Core Functionality
- **Voter Registration** - Secure voter enrollment with RSA key generation
- **Anonymous Voting** - Token-based voting system ensuring voter privacy
- **Vote Encryption** - Multi-layer encryption (AES + RSA) for vote confidentiality
- **Result Tallying** - Secure vote counting with integrity verification
- **Security Demonstration** - Interactive showcase of cryptographic operations

### Security Features
- **Digital Signatures** - RSA signatures for token authentication
- **Hash Verification** - SHA-256 integrity checking
- **Double Voting Prevention** - One-time token usage enforcement
- **Eligibility Verification** - Voter qualification checking
- **Tamper Detection** - Cryptographic integrity validation

## 🔐 Security Properties

### 1. Integrity Protection
```
✅ SHA-256 hashing of vote content
✅ Digital signatures on authentication tokens  
✅ Hash verification during vote counting
✅ Cryptographic proof against vote modification
```

### 2. Voter Anonymity
```
✅ Token-based authentication separates voter ID from vote
✅ Anonymous vote storage without voter identification
✅ Unlinkable vote submission process
✅ Privacy-preserving result tallying
```

### 3. Vote Confidentiality
```
✅ AES-128 encryption for vote content
✅ RSA-1024 encryption for AES keys
✅ Layered encryption protection
✅ Secure key management
```

### 4. Voter Authentication
```
✅ Digital token signatures from Election Authority
✅ Double voting prevention through token tracking
✅ Voter eligibility verification
✅ RSA signature verification on all tokens
```

## 📁 Project Structure

```
SecureVotingSystem/
├── src/
│   ├── crypto/
│   │   ├── RSAKeyManager.java      # RSA encryption/decryption utilities
│   │   ├── AESUtil.java            # AES symmetric encryption
│   │   ├── DigitalSignature.java   # Digital signature operations
│   │   └── HashUtil.java           # SHA-256 hashing and salt generation
│   ├── model/
│   │   ├── Voter.java              # Voter data model
│   │   ├── Vote.java               # Vote data structure
│   │   ├── VoterToken.java         # Authentication token model
│   │   └── Election.java           # Election configuration
│   ├── service/
│   │   ├── ElectionAuthority.java  # Voter registration & token issuance
│   │   ├── VotingService.java      # Vote submission & tallying
│   │   └── DataStorage.java        # In-memory data management
│   └── VotingSystemMain.java       # Main application interface
├── lib/
│   └── commons-codec-1.15.jar     # Base64 encoding library
├── build/                          # Compiled classes (generated)
└── README.md                       # This file
```

## 👨‍💻 Authors

- **Your Name** - *Initial work* - [YourGitHub](https://github.com/PereraKRD/Information_Security_Project_Secured_Voting_System.git)
