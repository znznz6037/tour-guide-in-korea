import { useNavigate } from "react-router-dom";

export default function GuideList() {

    const navigate = useNavigate();

    const toForm = () => {
        navigate('/guides/new');
    }

    return (
        <div>
            <h1>Guides</h1>
            <button onClick={toForm}>Create Trip</button>
        </div>
    );
}