import React, {useState, useEffect, useCallback} from "react"    // import useState & useEffect from "react"
import logo from './logo.svg';
import './App.css';
import axios from "axios"
import {useDropzone} from 'react-dropzone'

// Playing with "hooks", just because its cool:
// The following 'UserProfiles' is a functioning component used in our 'App()' function (see bottom of this file).
// AW: this notation looks remeniscent of lambdas in C++.
const UserProfiles = () => {

  //#^ Sec 14: Now that we have the data - let's set the state:
  const [userProfiles, setUserProfiles] = useState([]); // Initial state is empty array. 'userProfiles' is the state, and this comes from 'setUserProfiles'
    //#^ AW: Sec 14: lotta magic: what's happening here? - Where is "userProfiles" coming from? Axios?

  const fetchUserProfiles = () =>{  // invoke axios
    // Fetch the users  from our backend:
    // .then returns a promise, and =>res {...} returns a response
    axios.get("http://localhost:8080/api/v1/user-profile").then(res => {
      // At end of section "13 - Components and Axios - 00:53:36" - just log the response for diagnostic purposes.
      console.log(res);
      //#^  Render user data to browser client proper ("Section 14 - Rendering User Profile - 01:04:50").
      //const data = res.data;  // Access just the 'data' portion of the HTML response.
      setUserProfiles(res.data);  // let's set the state: Access just the 'data' portion of the HTML response.
    }); 
  }

  useEffect(() => {
    fetchUserProfiles();  // "just to component deep mount if you've ever done any kind of react" (AW: not sure I have...)
  }, []); // at end we also - for now - pass empty array; just means that "if there is anything that changes in this list the 'useEffect()' will be triggered again"

  //return <h1>hello</h1>;  // For now return just this: we MUST return something or the browser complains that "a return statement is missing".
  //#^  Render user data to browser client proper ("Section 14 - Rendering User Profile - 01:04:50").
  return userProfiles.map((userProfile, index) => {
    //#^ AW: Sec 14: lotta magic: what's happening here? - Where is "userProfile" coming from? Axios?
    return (
      <div key={index}>
        {/* todo: profile image */}
        <br/>
        <br/>
        <h1>{userProfile.username}</h1>
        <p>{userProfile.userProfileId}</p>
        <Dropzone />
        <br/>
      </div>
    );
  });
};

function Dropzone() {
  const onDrop = useCallback(acceptedFiles => {
    // Do something with the files
    //#+ (15 - React Dropzone - 01:09:20)
    const file = acceptedFiles[0];
    console.log(file);
  }, [])
  const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      {
        isDragActive ?
          <p>Drop the image here ...</p> :
          <p>Drag 'n' drop profile image, or click to select profile image</p>
      }
    </div>
  )
}

function App() {
  return (
    <div className="App">
      <UserProfiles />
    </div>
  );
}

export default App;
