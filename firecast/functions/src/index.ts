import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import * as express from 'express';
import * as axiosLib from 'axios';
import * as auth from './auth';
import * as redis from 'redis';


admin.initializeApp()

const REDIS_HOST = '10.134.25.67'
const REDIS_PORT = 6379
const cache = redis.createClient(REDIS_PORT, REDIS_HOST)

const fromCache = (key:string):Promise<string|null>=> new Promise(async resolve => {
  if(cache.connected===false){
      resolve(null)
  }else{
    return await cache.get(key, function(_err, reply){
      if(_err){
        resolve(null)
      }else{
        resolve(reply)
      }
    })
  }
})



// Functions called directly

// https://us-central1-fir-dc83e.cloudfunctions.net/addEmployee

export const addEmployee = functions.https.onRequest(async (request, response) => {
  try{
    
    functions.logger.info("Function call: addEmployee");
    const MAX_NUMBER_OF_EMPLOYERS = 2
    
    //Retrieving userId and companyId from request body
    const body = request.body.data
    functions.logger.info(`Body: ${JSON.stringify(body)}`);
    const userId = body.userId
    const companyId = body.companyId
    functions.logger.info(`userId: ${userId}, companyId: ${companyId}`);

    //Getting all the employers of the user from firestore
    const employees = await admin.firestore().collection(`employees`).where('userId', '==', userId).get()
    functions.logger.info(`employees: ${JSON.stringify(employees)}`);


    if(employees.size < MAX_NUMBER_OF_EMPLOYERS){
      //Checking, if user is already employed at the company
      for(let i = 0; i < employees.docs.length; i++){
        if(employees.docs[i].data().companyId === companyId){
          functions.logger.info('The user is already employed at this company');
          response.status(500).send({error: {"code": 501, "message": "The user is already employed at this company"} })
          return
        }
      }

      //Getting user and company from firestore with parallel requests
      const userPromise = admin.firestore().doc(`users/${userId}`).get()
      const companyPromise = admin.firestore().doc(`companies/${companyId}`).get()
      const user =  (await userPromise).data()
      const company = (await companyPromise).data()
      functions.logger.info(`user: ${JSON.stringify(user)}`);
      functions.logger.info(`company: ${JSON.stringify(company)}`);


      if(user && company){
        // Creating employee object and sending to firestore
        const newEmployeeRef = admin.firestore().collection('employees').doc();
        const employee = {
          id: newEmployeeRef.id,
          userId: userId,
          companyId: companyId,
          firstName: user.firstName,
          lastName: user.lastName,
          avatarUrl: user.avatarUrl,
          companyName: company.name
        }
        functions.logger.info(`employee: ${JSON.stringify(employee)}`);
        await newEmployeeRef.set(employee)
        
        //Sending response to the client
        functions.logger.info("Sending response");
        response.status(200).send({data:employee})
      }else{
        //User or company were was not found
        functions.logger.error("The user or the company is missing");
        response.status(500).send({error: {"code": 500, "message": "The user or the company is missing"} })
      }
    }else{
      //The user already has 2 employers
      functions.logger.error(`The user is already employed at ${MAX_NUMBER_OF_EMPLOYERS} companies`);
      response.status(500).send({
        error: {
          "code": 500,
          "message": `The user is already employed at ${MAX_NUMBER_OF_EMPLOYERS} companies`
        }
      })
    }
  }catch(err){
    //Something went wrong ¯\_(ツ)_/¯
    functions.logger.error(err);
    response.status(500).send({error : {"code": 500,"message": err.message}})
  }
});















// Triggers

// onCreate	Triggered when a document is written to for the first time.
// onUpdate	Triggered when a document already exists and has any value changed.
// onDelete	Triggered when a document with data is deleted.
// onWrite	Triggered when onCreate, onUpdate or onDelete is triggered.




//Called when new user is created
export const onUserCreate = functions.firestore
.document('/users/{userId}')
.onCreate((snapshot, context) => {
 
  //Logging userId
  functions.logger.info(`User with userId: ${context.params.userId}, was created`);
  
  //Setting user's createdAt and updatedAt fields
  const now = new Date()
  return snapshot.ref.update({createdAt : now, updatedAt: now, points : 100})
});


//Called when new user is updated
export const onUserUpdate = functions.firestore
.document('/users/{userId}')
.onUpdate((change, context) => {

  //data before change
  const before = change.before.data()

  //data after change
  const after = change.after.data()

  //checks prevent infinite loop
  if(before.firstName !== after.firstName
    || before.lastName !== after.lastName
    || before.avatarUrl !== after.avatarUrl
    || before.points !== after.points){
      //Logging event and userId
      functions.logger.info(`User with userId: ${context.params.userId}, was updated`);

      //Setting user's updatedAt fields
      const now = new Date()
      return change.after.ref.update({updatedAt: now})
  }else{
    return null
  }
});















// REST with Firebase Functions
// Base: https://us-central1-fir-dc83e.cloudfunctions.net/api/ 
const app = express();



//Create user

// Base: https://us-central1-fir-dc83e.cloudfunctions.net/api/users
app.post('/users', async (req, res) => {
  try{
    //header : req.header('headerName')
    //path : req.params.pathParamName
    //query : req.query.queryParamName
    const user = req.body;
    const newUserRef = admin.firestore().collection('users').doc();
    await newUserRef.set(user)
    user.id = newUserRef.id
    res.status(201).send({data:user});
  }catch(err){
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
});

//Update user
app.put("/users/:id", async (req, res) => {
  try{
    const body = req.body;
    const userId = req.params.id
    await admin.firestore().collection('users').doc(userId).update(body);
    res.status(200).send({"data":userId})
  }catch(err){
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
});

//Delete User
app.delete("/users/:id", async (req, res) => {
  try{
    const userId = req.params.id
    await admin.firestore().collection("users").doc(userId).delete();
    res.status(200).send({"data":userId});
  }catch(err){
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
})

//Get all users
app.get('/users', async (req, res) => {
  try{
    const snapshot = await admin.firestore().collection('users').get();
    const users : any[] = []
    snapshot.forEach((doc)=>{
      const id = doc.id
      const userData = doc.data()
      users.push({id, ...userData})
    })
    res.setHeader("Content-Type", "application/json")
    res.status(200).send(JSON.stringify(users));
  }catch(err){
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
});

//Get user
app.get("/users/:id", async (req, res) => {
  try{
    const userId = req.params.id
    const cachedUser = await fromCache(userId)
    if(cachedUser){
      res.status(200).send(Object.assign({}, JSON.parse(cachedUser), {from:'redis'}));
    }else{
      const userSnap = await admin.firestore().collection('users').doc(req.params.id).get();
      const user = userSnap.data()
      if(user){
        user.id = userId
        if(cache.connected == true){
          await cache.set(userId, JSON.stringify(user))
        }
        res.status(200).send(user);
      }else{
        res.status(500).send({error : {"code": 501,"message": `No user found with id: ${req.params.id}`}})
      }
    }
  }catch(err){
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
})

//Get all companies
app.get('/companies', async (req, res) => {
  try{
    const snapshot = await admin.firestore().collection('companies').get();
    const companies : any[] = []
    snapshot.forEach((doc)=>{
      const id = doc.id
      const companyData = doc.data()
      companies.push({id, ...companyData})
    })
    res.setHeader("Content-Type", "application/json")
    res.status(200).send(companies);
  }catch(err){
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
});




















// Sending requests with Axios

//Referencing Axios with default config
const axios = axiosLib.default

app.get("/githubUsers", async (req, res) => {
  try{
    //Getting GitHub users from the API
    const response = await axios.get('https://api.github.com/users')
    functions.logger.info(response.data);
    
    //Sending response
    res.status(200).send(response.data);
  }catch(err){
    //Something went wrong ¯\_(ツ)_/¯
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
})












//Same request with authentication requirement
app.get("/githubUsersWithAuth", auth, async (req, res) => {
  try{
    //Getting GitHub users from the API
    const response = await axios.get('https://api.github.com/users')
    functions.logger.info(response.data);
    
    //Sending response
    res.status(200).send(response.data);
  }catch(err){
    //Something went wrong ¯\_(ツ)_/¯
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
})



//If uncommented, all the app requests will require authentication
// app.use(auth)
















//Batch operation example
//Updae user name in both users and employees collections
app.post("/users/:id/first-name", async (req, res) => {
  try{
    //Retreiving userId and updated name
    const firstName = req.body.firstName;
    const userId = req.params.id
    const userRef = admin.firestore().collection('users').doc(userId)

    //If user is also an employee, we should update the docs with batch request
    const employees = await admin.firestore().collection(`employees`).where('userId', '==', userId).get()

    if(!employees.empty){
      //User is employed at least in 1 company, so we should update his name everywhere with batch request
      const batch = admin.firestore().batch()
      batch.update(userRef, {firstName : firstName})
      employees.forEach((employee) => {
        batch.update(admin.firestore().collection('employees').doc(employee.id), {firstName:firstName})
      })
      await batch.commit()
    }else{
      //User is not employed anywhere, so we should update only user document
      await userRef.update({firstName : firstName});
    }
    res.status(200).send({"data":userId})
  }catch(err){
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
});











//Transaction example
//Transfer points from one user to another
app.post("/users/:id/transfer", async (req, res) => {
  try{
    functions.logger.info("Transfer function called");

    const transferAmount = req.body.points;
    const fromUserId = req.params.id;
    const toUserId = req.body.toUserId;

    functions.logger.info(`Transfer amount:${transferAmount}, from user: ${fromUserId}, to user: ${toUserId}`);

    await admin.firestore().runTransaction(async t => {
      //Retreiving users that make the transfer
      const fromUserRef = admin.firestore().collection('users').doc(fromUserId)
      const toUserRef = admin.firestore().collection('users').doc(toUserId)
      const fromUserPromise = t.get(fromUserRef)
      const toUserPromise = t.get(toUserRef)
      const fromUser = (await fromUserPromise).data()
      const toUser = (await toUserPromise).data()
      functions.logger.info(`From user data: ${JSON.stringify(fromUser)}`);
      functions.logger.info(`To user data: ${JSON.stringify(toUser)}`);
      if(!fromUser) throw new Error('Invalid remitter id')
      if(!toUser) throw new Error('Invalid receiver id')

      //Check if remmiter has available points 
      if(fromUser.points >= transferAmount){
        //update points for both users
        t.update(fromUserRef, {points : (fromUser.points - transferAmount)}) 
        t.update(toUserRef, {points : (toUser.points + transferAmount)}) 
      }else{
        throw new Error('Insufficient funds')
      }
    })
    res.status(200).send({"data":"ok"})
  }catch(err){
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
});




//on each request endpoints will be checked and, if applicable, then triggered 
exports.api = functions.https.onRequest(app);
















//Scheduled functions with Cron 

// exports.scheduledFunction = functions.pubsub.schedule('* * * * *').onRun(async (context) => {
//   await admin.firestore().collection('timers').doc('timer1').update({"time": admin.firestore.Timestamp.now()});
//   return console.log('success')
// });
