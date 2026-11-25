import React from 'react'
import { Link } from 'react-router-dom'

function LibraryHeader() {

    const libHeader = "Library Management System"

    return (
        <div>
            <header>
                <h1>{libHeader}</h1>

                <nav>
                    <Link to="/">HOME</Link>
                    {' | '}
                    <Link to="/books">BOOKS</Link>
                    {' | '}
                    <Link to="/Office">OFFICE</Link>
                    {' | '}
                    <Link to="/doctor">DOCTORS</Link>
                    {' | '}
                    <Link to="/login">LOGIN</Link>
                </nav>

                <p>
                    This is a library system that will help you
                    to manage books.
                </p>
            </header>
        </div>
    )
}

export default LibraryHeader