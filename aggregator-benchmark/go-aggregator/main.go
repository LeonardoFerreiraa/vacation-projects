package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"github.com/gorilla/mux"
	"net/http"
)

func main() {
	router := mux.NewRouter().StrictSlash(true)

	router.HandleFunc("/aggregations", aggregationsHandler)

	_ = http.ListenAndServe(":8080", router)
}

func aggregationsHandler(writer http.ResponseWriter, _ *http.Request) {
	todoCh := make(chan TodoResponse)
	uuidCh := make(chan UUIDResponse)

	go requestTodo(todoCh)
	go requestUUID(uuidCh)

	todo := <-todoCh
	uuid := <-uuidCh

	_ = json.NewEncoder(writer).Encode(AggregatedResponse{
		TodoTitle: todo.Title,
		UUID:      uuid.UUID,
	})
}

func requestUUID(ch chan UUIDResponse) {
	resp, _ := http.Get("https://httpbin.org/uuid")

	var uuidResponse UUIDResponse
	_ = json.NewDecoder(resp.Body).Decode(&uuidResponse)

	ch <- uuidResponse
}

func requestTodo(ch chan TodoResponse) {
	anything := requestAnything()

	resp, _ := http.Get(fmt.Sprintf("https://jsonplaceholder.typicode.com/todos/%d", anything.JSON.Number))

	var todoResponse TodoResponse
	_ = json.NewDecoder(resp.Body).Decode(&todoResponse)

	ch <- todoResponse
}

func requestAnything() AnythingResponse {
	anythingRequest := AnythingRequest{Number: 200}
	request, _ := json.Marshal(anythingRequest)

	resp, _ := http.Post("https://httpbin.org/anything", "application/json", bytes.NewBuffer(request))

	var anythingResponse AnythingResponse
	_ = json.NewDecoder(resp.Body).Decode(&anythingResponse)

	return anythingResponse
}
