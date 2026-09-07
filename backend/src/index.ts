import express from "express";
import cors from 'cors';
import routes from './routes/index.ts';
import dotenv from 'dotenv';

dotenv.config()

const app = express()
const PORT = process.env.PORT

app.use(express.json())
app.use(express.urlencoded({ extended: true }))

app.use(cors())

app.use('/api', routes)


app.listen(PORT, () => {
    console.log(`Server started! Running on http://localhost:${PORT}`)
})