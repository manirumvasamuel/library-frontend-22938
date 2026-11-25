import React from 'react';

function LibraryCards({ bookInfo }) {
    return (
        <div style={{ border: "3px solid blue", marginTop:"3px" }}>
            <div>{bookInfo.code}</div>
            <div>{bookInfo.name}</div>
            <div>{bookInfo.price}</div>
            </div>
            );
            }
            export default LibraryCards;