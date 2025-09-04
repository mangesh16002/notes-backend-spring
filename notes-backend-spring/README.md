# Notes Backend (Spring Boot)

Endpoints (base path `/api`):

- `GET /api/notes` — list notes
- `POST /api/notes` — create note `{ "title": "...", "content": "..." }`
- `GET /api/notes/{id}` — get one
- `PUT /api/notes/{id}` — update `{ "title": "...", "content": "..." }`
- `DELETE /api/notes/{id}` — delete
- `POST /api/notes/{id}/share` — returns `{ token, publicUrl }`
- `GET /api/public/{token}` — fetch shared note

## Deploy on Render (Docker)
1. Push this repo to GitHub.
2. On Render: New → Web Service → pick this repo. Select **Docker**.
3. Set env var `ALLOWED_ORIGINS` to your Vercel domain (e.g. `https://your-app.vercel.app`) or `*` for quick testing.
4. Deploy. Use the URL as `VITE_API_BASE` + `/api` in the frontend.
