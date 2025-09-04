import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import axios from 'axios';

function SharedNote() {
  const { id } = useParams();
  const [note, setNote] = useState(null);

  useEffect(() => {
    axios.get(`${import.meta.env.VITE_API_BASE}/notes/shared/${id}`)
      .then(res => setNote(res.data))
      .catch(err => console.error(err));
  }, [id]);

  if (!note) return <p>Loading...</p>;

  return (
    <div>
      <h2>{note.title}</h2>
      <p>{note.content}</p>
    </div>
  );
}

export default SharedNote;
