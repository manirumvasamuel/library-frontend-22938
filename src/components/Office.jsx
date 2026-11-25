import { useEffect, useState } from "react";
import axios from "axios";

function Office() {
  const [offices, setOffices] = useState([]);
  const [search, setSearch] = useState("");

  const baseUrl = "http://localhost:8080/api/offices";

  useEffect(() => {
    axios
      .get(`${baseUrl}/all`)
      .then((res) => {
        setOffices(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  const handleDelete = (id) => {
    axios
      .delete(`${baseUrl}/delete/${id}`)
      .then(() => {
        setOffices(offices.filter((item) => item.id !== id));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const filteredOffices = offices.filter((item) => {
    const text = search.toLowerCase();
    const num = String(item.officeNumber || "").toLowerCase();
    const officeName = String(item.name || "").toLowerCase();
    return num.includes(text) || officeName.includes(text);
  });

  return (
    <div>
      <h2>Offices</h2>

      <input
        type="text"
        placeholder="Search by office name or number"
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />

      <table border="1">
        <thead>
          <tr>
            <th>ID</th>
            <th>Office Number</th>
            <th>Office Name</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {filteredOffices.map((office) => (
            <tr key={office.id}>
              <td>{office.id}</td>
              <td>{office.officeNumber}</td>
              <td>{office.name}</td>
              <td>
                <button onClick={() => handleDelete(office.id)}>Delete</button>
                <button>Update</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Office;