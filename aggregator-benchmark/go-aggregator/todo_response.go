package main

type TodoResponse struct {
	ID        int64  `json:"id"`
	UserID    int64  `json:"userId"`
	Title     string `json:"title"`
	Completed bool   `json:"completed"`
}
