package com.example.notes.controller;

import com.example.notes.dto.NoteRequest;
import com.example.notes.model.Note;
import com.example.notes.repo.NoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class NotesController {
    private final NoteRepository repo;

    public NotesController(NoteRepository repo) { this.repo = repo; }

    @GetMapping("/notes")
    public List<Note> list() {
        return repo.findAll();
    }

    @PostMapping("/notes")
    public ResponseEntity<Note> create(@RequestBody NoteRequest req) {
        Note n = new Note();
        n.setTitle(req.getTitle() == null ? "" : req.getTitle());
        n.setContent(req.getContent() == null ? "" : req.getContent());
        Note saved = repo.save(n);
        return ResponseEntity.created(URI.create("/api/notes/" + saved.getId())).body(saved);
    }

    @GetMapping("/notes/{id}")
    public ResponseEntity<Note> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<Note> update(@PathVariable Long id, @RequestBody NoteRequest req) {
        return repo.findById(id).map(n -> {
            if (req.getTitle() != null) n.setTitle(req.getTitle());
            if (req.getContent() != null) n.setContent(req.getContent());
            return ResponseEntity.ok(repo.save(n));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notes/{id}/share")
    public ResponseEntity<?> share(@PathVariable Long id) {
        Optional<Note> opt = repo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Note n = opt.get();
        String token = n.getPublicToken();
        if (token == null || token.isEmpty()) {
            token = UUID.randomUUID().toString();
            n.setPublicToken(token);
            repo.save(n);
        }
        return ResponseEntity.ok().body(java.util.Map.of(
                "token", token,
                "publicUrl", "/api/public/" + token
        ));
    }

    @GetMapping("/public/{token}")
    public ResponseEntity<Note> publicNote(@PathVariable String token) {
        return repo.findByPublicToken(token).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
