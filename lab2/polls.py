from fastapi import FastAPI, HTTPException, Body
from pydantic import BaseModel
from typing import List

app = FastAPI()


class Option(BaseModel):
    id: int
    description: str


class Poll(BaseModel):
    id: int
    question: str
    options: List[Option]


class Vote(BaseModel):
    vote_id: int
    option_id: int



polls = []
votes = []


@app.post("/poll/", response_model=Poll)
async def create_poll(poll: Poll = Body(...)):
    poll.id = len(polls) + 1  # Simple ID generation
    polls.append(poll)
    return poll


@app.get("/poll/", response_model=List[Poll])
async def get_polls():
    return polls


@app.get("/poll/{id}", response_model=Poll)
async def get_poll(id: int):
    for poll in polls:
        if poll.id == id:
            return poll
    raise HTTPException(status_code=404, detail="Poll not found")


@app.put("/poll/{id}", response_model=Poll)
async def update_poll(id: int, poll_update: Poll = Body(...)):
    for index, poll in enumerate(polls):
        if poll.id == id:
            polls[index] = poll_update
            return poll_update
    raise HTTPException(status_code=404, detail="Poll not found")


@app.delete("/poll/{id}")
async def delete_poll(id: int):
    global polls
    polls = [poll for poll in polls if poll.id != id]
    return {"message": "Poll deleted"}


@app.post("/poll/{id}/vote/")
async def cast_vote(id: int, vote: Vote):
    for poll in polls:
        if poll.id == id:
            for option in poll.options:
                if option.id == vote.option_id:
                    votes_count_for_poll = len([v for v in votes if v['poll_id'] == id])
                    vote.vote_id = votes_count_for_poll + 1
                    votes.append({"poll_id": id, "option_id": vote.option_id, "vote_id": vote.vote_id})
                    return {"message": "Vote casted"}
    raise HTTPException(status_code=404, detail="Invalid poll ID or option ID")

@app.get("/poll/{id}/vote/")
async def get_votes(id: int):
    poll_votes = [vote for vote in votes if vote['poll_id'] == id]
    return poll_votes


@app.get("/poll/{id}/vote/{vote_id}")
async def get_vote(id: int, vote_id: int):
    for vote in votes:
        if vote['poll_id'] == id and vote['vote_id'] == vote_id:
            return vote
    raise HTTPException(status_code=404, detail="Vote not found")

@app.put("/poll/{id}/vote/{vote_id}")
async def update_vote(id: int, vote_id: int, vote: Vote):
    for index, v in enumerate(votes):
        if v['poll_id'] == id and v['vote_id'] == vote_id:
            votes[index] = vote
            return vote
    raise HTTPException(status_code=404, detail="Vote not found")

@app.delete("/poll/{id}/vote/{vote_id}")
async def delete_vote(id: int, vote_id: int):
    global votes
    votes = [v for v in votes if not (v['poll_id'] == id and v['vote_id'] == vote_id)]
    return {"message": "Vote deleted"}


@app.get("/poll/{id}/results/")
async def get_results(id: int):
    results = {}
    for vote in votes:
        if vote['poll_id'] == id:
            option_id = vote['option_id']
            if option_id in results:
                results[option_id] += 1
            else:
                results[option_id] = 1
    return results

