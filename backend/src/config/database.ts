import { Pool } from 'pg';
import dotenv from 'dotenv';

dotenv.config()

const pool = new Pool({
    host: process.env.POSTGRES_HOST,
    port: 5432,
    database: process.env.DATABASE_NAME,
    user: process.env.POSTGRES_USER,
    password: process.env.POSTGRES_PASSWORD
})

pool.connect((err, client, release) => {
    if (err) {
        return console.error('Error acquiring client', err.stack);
    }
    console.log('Database connected successfully');
    release();
});

export default pool