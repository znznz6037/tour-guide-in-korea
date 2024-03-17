import { useNavigate } from "react-router-dom";

export default function Home() {
    const navigate = useNavigate();

    const toOAuth2 = () => {
        navigate('/oauth2');
    }

    const toProfile = () => {
        navigate('/profile');
    }

    const toBoard = () => {
        navigate('/guides');
    }

    return(
        <div>
            <span onClick={toBoard}>Home</span> 
            <button onClick={toProfile}>Profile</button> 
            <button onClick={toOAuth2}>Login</button>
            <button onClick={toBoard}>Guides</button>
        </div>
    )
}