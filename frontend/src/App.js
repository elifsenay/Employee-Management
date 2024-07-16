// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import EmployeeEntry from './EmployeeEntry';
import LeaveEntry from './LeaveEntry';
import EmployeeList from './EmployeeList';

function App() {
  return (
      <Router>
        <div className="App">
          <Switch>
            <Route path="/employee-entry" component={EmployeeEntry} />
            <Route path="/leave-entry" component={LeaveEntry} />
            <Route path="/employee-list" component={EmployeeList} />
          </Switch>
        </div>
      </Router>
  );
}

export default App;
