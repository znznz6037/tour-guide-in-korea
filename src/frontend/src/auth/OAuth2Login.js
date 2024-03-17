import axios from "axios";
import { useEffect } from "react";

export default function OAuth2Login() {
    useEffect(() => {
        axios.get('/oauth2/login')
        .then((response) => {
            console.log(response.data);
            window.location.href = response.data;
        })
        .catch((error) => {
            console.log(error);
        })

    }, []);
}