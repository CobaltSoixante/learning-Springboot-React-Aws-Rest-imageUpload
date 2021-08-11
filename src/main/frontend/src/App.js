import React, {useState, useEffect} from "react"    // import useState & useEffect from "react"
import logo from './logo.svg';
import './App.css';
import axios from "axios"

// Playing with "hooks", just because its cool:
// The following 'UserProfiles' is a functioning component used in our 'App()' function (see bottom of this file).
// AW: this notation looks remeniscent of lambdas in C++.
const UserProfiles = () => {

  const fetchUserProfiles = () =>{  // invoke axios
    // .then returns a promise, and =>res {...} returns a response
    axios.get("http://localhost:8080/api/v1/user-profile").then(res => {
      console.log(res); // for now - just log the response.
    }); 
  }

  useEffect(() => {
    fetchUserProfiles();  // "just to component deep mount if you've ever done any kind of react" (AW: not sure I have...)
  }, []); // at end we also - for now - pass empty array; just means that "if there is anything that changes in this list the 'useEffect()' will be triggered again"

  return <h1>hello</h1>;  // For now return just this: we MUST return something or the browser complains that "a return statement is missing".
}

function App() {
  return (
    <div className="App">
      <UserProfiles />
    </div>
  );
}

export default App;
