import React from "react";
import "./HomePage.css"; // Import the custom CSS file for additional styling

const HomePage = () => {
  return (
    <div className="d-flex flex-column min-vh-100 bg-light text-dark">
      {/* Navigation Bar */}
      <nav className="navbar navbar-expand-lg navbar-light bg-white border-bottom">
        <a className="navbar-brand" href="#">
          FundFlow
        </a>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav mr-auto">
            <li className="nav-item">
              <a className="nav-link" href="#">
                Home
              </a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="#">
                Expense Tracker
              </a>
            </li>
            <li className="nav-item">
              <a className="nav-link btn btn-outline-primary" href="#">
                Investment Tracker
              </a>
            </li>
            <li className="nav-item">
              <a className="nav-link btn btn-outline-primary" href="#">
                About Us
              </a>
            </li>
          </ul>
          <ul className="navbar-nav ml-auto">
            <li className="nav-item">
              <a className="nav-link btn btn-primary text-white" href="#">
                Sign In/Register
              </a>
            </li>
          </ul>
        </div>
      </nav>

      {/* Hero Section */}
      <div
        className="jumbotron bg-light text-center text-dark"
        style={{ padding: "4rem 2rem" }}
      >
        <h1 className="display-4">Empower Your Financial Journey</h1>
        <p className="lead">
          Simplify expense tracking, manage investments, and make informed
          decisions with FundFlow. Our platform offers tools to seamlessly track
          your spending, optimize your investment portfolio, and access expert
          insights to guide your financial growth.
        </p>
        <a className="btn btn-primary btn-lg" href="#" role="button">
          Start Now
        </a>

      </div>

      {/* Features Section */}
      <div className="container my-5 flex-grow-1">
        <div className="row text-center">
          <div className="col-md-4">
            <div className="card bg-white text-dark p-4 shadow-sm h-100">
              <h3 className="card-title">Track Expenses</h3>
              <p className="card-text">
                Easily monitor and categorize your daily expenses to stay on top
                of your budget.
              </p>
            </div>
          </div>
          <div className="col-md-4">
            <div className="card bg-white text-dark p-4 shadow-sm h-100">
              <h3 className="card-title">Manage Investments</h3>
              <p className="card-text">
                Keep track of your investments, view performance metrics, and
                optimize your portfolio.
              </p>
            </div>
          </div>
          <div className="col-md-4">
            <div className="card bg-white text-dark p-4 shadow-sm h-100">
              <h3 className="card-title">Invest Smartly</h3>
              <p className="card-text">
                Get tailored investment recommendations and insights to make
                informed decisions.
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Footer */}
      <footer className="bg-light text-dark text-center p-4 mt-auto border-top">
        <p>© 2024 FundFlow. All rights reserved.</p>
        <p>
          <a href="#" className="text-primary mx-2">
            Privacy Policy
          </a>{" "}
          |
          <a href="#" className="text-primary mx-2">
            Terms of Service
          </a>
        </p>
        <p>
          <a href="#" className="text-primary mx-2">
            About
          </a>{" "}
          |
          <a href="#" className="text-primary mx-2">
            Contact
          </a>
        </p>
      </footer>
    </div>
  );
};

export default HomePage;
