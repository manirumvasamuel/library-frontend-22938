import { BrowserRouter, Routes, Route } from 'react-router-dom'
import './App.css'

import LibraryBooks from './components/LibraryBooks'
import LibraryLogin from './components/LibraryLogin'
import LibraryHeader from './components/LibraryHeader'
import Office from './components/Office'

function App() {
  return (
    <BrowserRouter>
      <LibraryHeader />

      <Routes>
        <Route path="/" element={<Office />} />
        <Route path="/login" element={<LibraryLogin />} />
        <Route path="/offices" element={<Office />} />
        <Route path="/Office" element={<Office />} />
        <Route path="/books" element={<LibraryBooks />} />
        <Route path="*" element={<Office />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App