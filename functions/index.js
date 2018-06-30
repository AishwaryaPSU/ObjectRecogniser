const functions = require('firebase-functions');
const vision = require('@google-cloud/vision')({
    projectId: 'objectrecognizer-205306',
    keyfileName: 'keyfile.json'
});
const admin = require('firebase-admin');
admin.initializeApp();
// Create the Firebase reference to store our image data
const db = admin.database();


// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
 exports.helloWorld = functions.https.onRequest((request, response) => {
  response.send("Hello from Firebase!");
 });

exports.callVision = functions.storage.object().onFinalize(event => {
        console.log("event ",event);
        const obj = event;

        const gcsUrl = "gs://" + obj.bucket + "/" + obj.name;
        console.log("gcsUrl ",gcsUrl);
        const imageName = obj.name;
        const revisedImageName = imageName.replace(/-/g,"").replace(".jpg","");
        console.log("imageName ",imageName," revisedImageName ",revisedImageName);
        const imagesRef = db.ref(revisedImageName);
        return Promise.resolve()
            .then(() => {
            let visionReq = {
                "image": {
                    "source": {
                        "imageUri": gcsUrl
                    }
                },
                "features": [
                    {
                        "type": "FACE_DETECTION"
                    },
                    {
                        "type": "LABEL_DETECTION"
                     },
                     {
                        "type": "TEXT_DETECTION"
                     },
                     {
                         "type": "LANDMARK_DETECTION"
                     },
                     {
                       "type": "LOGO_DETECTION"
                     },
                      {
                        "type": "IMAGE_PROPERTIES"
                       },
                       {
                       "type": "SAFE_SEARCH_DETECTION"
                     }
                ]
            }
            return vision.annotate(visionReq);
          })
          .then(([visionData]) => {
            console.log('got vision data: ', visionData[0]);
            imagesRef.push(visionData[0]);
            return detectEntities(visionData[0]);
          })
          .then(() => {
            console.log(`Parsed vision annotation and wrote to Firebase`);
            return null;
          });
});

function detectEntities(imageData){

    return null;
}