# Error Code filter 

## Running Locally 
- Java 21 
- ngrok 

... setup bits and run the java program 

### Setting up ngrok 
- https://ngrok.com/download 
- Setup authtoken, you'll need an account 
  - https://dashboard.ngrok.com/get-started/your-authtoken 
  - Configure this via command line as instructed 

### Running using ngrok 
- run the program using java, by default it'll be on port 8080 - so ensure this is free first
- In CLI, `ngrok http 8080`
- This should give you a http address, it'll look similar to 
`Forwarding  https://da7f-86-24-60-207.ngrok-free.app -> http://localhost:8080   `
- On your device (can be on any network), navigate to https://....ngrok-free.app/hello 

## Endpoints 
- `/hello` 
- `/select-machine` 
- `/select-error` 