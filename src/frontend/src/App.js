import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./Home";
import Profile from "./auth/Profile";
import { RequireToken } from "./auth/Auth";
import OAuth2Login from "./auth/OAuth2Login";
import LoginSuccess from "./auth/LoginSuccess";
import GuideList from "./guide/GuideList";
import GuideForm from "./guide/GuideForm";

function App() {
  return (
    <BrowserRouter>
      <Home/>
      <Routes>
        <Route path="/" element={<GuideList/>}/>
        <Route path="/guides" element={<GuideList/>}/>
        <Route path="/oauth2" element={<OAuth2Login/>}/>
        <Route path="/profile" element={
          <RequireToken>
            <Profile/>
          </RequireToken>
        }/>
        <Route path="/login/success/:code" element={<LoginSuccess/>}/>
        <Route path="/guides/new" element={<GuideForm/>}/>
      </Routes>
    </BrowserRouter>
  );
}

export default App;