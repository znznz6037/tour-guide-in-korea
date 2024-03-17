import axios from "axios";
import { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { setToken } from "./Auth";

export default function LoginSuccess() {
    const navigate = useNavigate();
    const params = useParams();

    useEffect(() => {
        setToken("Bearer " + params.code);
        navigate('/', {state : true});
    }, []);

    return (
        <div>
            <p>Login success</p>
        </div>
    );
}