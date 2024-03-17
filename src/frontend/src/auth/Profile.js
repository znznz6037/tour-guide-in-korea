import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getToken } from "./Auth";
import axios from "axios";

export default function Profile(){
    const navigate = useNavigate();
    const [userInfo, setUserInfo] = useState();

    useEffect(() => {
        let jwt = getToken();
        // console.log(jwt);
        // if(!jwt) {
        //     return;
        // }

        axios.get('/oauth2/profile', {
            headers: {
                'Authorization': jwt
            }
        })
        .then((response) => {
            if(response.data) {
                setUserInfo(response.data);
                console.log(response.data);
            }
        })
    }, []);

    const signOut = () => {
        localStorage.removeItem("token");
        navigate("/", {state : false});
    }

    return(
        <div>
            <h1>Profile</h1>
            {userInfo !== undefined ?  
                <div>
                    <img src={userInfo.picture} alt="Profile img"/>
                    <p>{userInfo.name}</p>
                    <p>{userInfo.email}</p>
                </div>
                : <p>Loading...</p>}
            <button type="button" onClick={signOut}>
                Sign Out
            </button>
        </div>
    )
}