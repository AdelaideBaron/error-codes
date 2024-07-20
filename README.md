# Error Code filter 
An app to display machines and error codes, and the possible solutions, on a simple website. 

# How to use
**⚠️⚠️ Any updates made to the error codes csv requires the app to be restarted ⚠️⚠️** 

## Technical requirements
- Java 21
- Maven
- _If wanting to expose beyond local device_, ngrok (see [setting up ngrok](#Setting up ngrok))

## Running the app
If using intelliJ, it is a simple SpringBoot app with no external dependencies - simply run the main application class `src/main/java/com/wes/error_codes/ErrorCodesApplication.java`
### From the command line using maven
1. Navigate to the root directory, e.g. `cd path/error-codes`
2. Run the app using mvn: `mvn compile` followed by `mvn exec:java` - this should be port 8080, if it is not, change the ngrok port below to match the port the app is running on
3. Open a new cmd window (keeping the other open, as that's where the app is running) and run `ngrok http 8080`
4. This ^ will open a new window, showing the url to access, e.g. `http://...ngrok-free.app`
5. You can access the app from this endpoint now, on any device (off the host network too)

## Updating the errors CSV 
- CSV to edit: `src/main/resources/data/machine_error_codes.csv`
- Do not edit the headers

  | Error or Error Message | Error Details      | Possible Causes         | Machine            |
  |------------------------|--------------------|-------------------------|--------------------|
  | ERROR_CODE_HERE        | ERROR_DETAILS      | potential_cause_1       | MACHINE_APPLICABLE |
  |                        |                    | potential_cause_2       |                    |
- | NEW_ERROR_CODE         | NEW_ERROR_DETAILS  | potential_cause_of_this | MACHINE_APPLICABLE |

- A new error code must have all fields populated for the first row
- Proceeding rows may contain possible causes, and should have all rows empty except possible causes

## Setup
### Setting up ngrok 
- https://ngrok.com/download 
- Setup authtoken, you'll need an account 
- https://dashboard.ngrok.com/get-started/your-authtoken 
- Configure this via command line as instructed
