import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import * as express from 'express';
import * as https from 'https';
import * as axios from 'axios';
// import * as cors from 'cors';

admin.initializeApp()
const app = express();
// app.use(cors({ origin: true }));


    //header : req.header('asd')
    //path : req.params.asd
    //query : req.query.asd
app.post('/users', async (req, res) => {
  try{
    const user = req.body;
    const newUserRef = admin.firestore().collection('users').doc();
    user.id = newUserRef.id
    await newUserRef.set(user)
    res.status(201).send({data:user});
  }catch(err){
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
});

app.put("/users/:id", async (req, res) => {
  try{
    const body = req.body;
    await admin.firestore().collection('users').doc(req.params.id).update(body);
    res.status(200).send({"data":"ok"})
  }catch(err){
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
});

app.delete("/users/:id", async (req, res) => {
  try{
    await admin.firestore().collection("users").doc(req.params.id).delete();
    res.status(200).send({"data":"ok"});
  }catch(err){
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
})

app.get('/users', async (req, res) => {
  try{
    const snapshot = await admin.firestore().collection('users').get();
    res.status(200).send(snapshot.docs);
  }catch(err){
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
});

app.get("/users/:id", async (req, res) => {
  try{
    const user = (await admin.firestore().collection('users').doc(req.params.id).get()).data();
    if(user){
      res.status(200).send(user);
    }else{
      res.status(500).send({error : {"code": 501,"message": `No user found with id: ${req.params.id}`}})
    }
  }catch(err){
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
})

app.get("/githubUsers", async (req, res) => {
  try{
    const user = (await admin.firestore().collection('users').doc(req.params.id).get()).data();
    if(user){
      res.status(200).send(user);
    }else{
      res.status(500).send({error : {"code": 501,"message": `No user found with id: ${req.params.id}`}})
    }
    
  }catch(err){
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
})



app.get('/companies', async (req, res) => {
  try{
    const snapshot = await admin.firestore().collection('companies').get();
    res.status(200).send(snapshot.docs);
  }catch(err){
    functions.logger.error(err);
    res.status(500).send({error : {"code": 501,"message": err.message}})
  }
});

exports.api = functions.https.onRequest(app);


// Start writing Firebase Functions
// https://firebase.google.com/docs/functions/typescript

// export const helloWorld = functions.https.onRequest((request, response) => {
//   functions.logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });

// class Employee{
//   id: string;
//   userId: string;
//   companyId: string;
//   firstName: string;
//   lastName: string;
//   avatarUrl: string;
//   companyName: string;

//   constructor(
//     id: string,
//     userId: string,
//     companyId: string,
//     firstName: string,
//     lastName: string,
//     avatarUrl: string,
//     companyName: string
//   ){
//     this.id = id
//     this.userId = userId
//     this.companyId = companyId
//     this.firstName = firstName
//     this.lastName = lastName
//     this.avatarUrl = avatarUrl
//     this.companyName = companyName
//   }
// }
// class User{
//     id: string;
//     firstName: string;
//     lastName: string;
//     avatarUrl: string

//     constructor(
//       id: string,
//       firstName: string,
//       lastName: string,
//       avatarUrl: string
//     ){
//       this.id = id
//       this.firstName = firstName
//       this.lastName = lastName
//       this.avatarUrl = avatarUrl

//     }
//   }
//   const userConverter = {
//     toFirestore: function(user:User) {
//         return {
//           id: user.id,
//           firstName: user.firstName,
//           lastName: user.lastName,
//           avatarUrl: user.avatarUrl
//         }
//     },
//     fromFirestore: function(snapshot:FirebaseFirestore.DocumentSnapshot<any>){
//         const data = snapshot.data();
//         return new User(data.id, data.firstName, data.lastName, data.avatarUrl)
//     }
// }

// class Company{
//   id: string;
//   name: string

//   constructor(
//     id: string,
//     name: string
//   ){
//     this.id = id
//     this.name = name
//   }
// }

// const companyConverter = {
//   toFirestore: function(company:Company) {
//       return {
//         id: company.id,
//         name: company.name
//       }
//   },
//   fromFirestore: function(snapshot:FirebaseFirestore.DocumentSnapshot<any>){
//       const data = snapshot.data();
//       return new Company(data.id, data.name)
//   }
// }



export const addEmployee = functions.https.onRequest(async (request, response) => {
  try{
    
    functions.logger.info("addEmployee called");
    const MAX_NUMBER_OF_EMPLOYERS = 2
    const body = request.body.data

    functions.logger.info(`Body: ${JSON.stringify(body)}`);
    const userId = body.userId
    const companyId = body.companyId
    functions.logger.info(`userId: ${userId}, companyId: ${companyId}`);
    const employees = await admin.firestore().collection(`employees`).where('userId', '==', userId).get()
    functions.logger.info(`employees: ${JSON.stringify(employees)}`);

    if(employees.size < MAX_NUMBER_OF_EMPLOYERS){
      for(let i = 0; i < employees.docs.length; i++){
        if(employees.docs[i].data().companyId === companyId){
          functions.logger.info('The user is already employed at this company');
          response.status(500).send({error: {"code": 501, "message": "The user is already employed at this company"} })
          return
        }
      }

      const userPromise = admin.firestore().doc(`users/${userId}`).get()
      const companyPromise = admin.firestore().doc(`companies/${companyId}`).get()
      const user =  (await userPromise).data()
      const company = (await companyPromise).data()

      functions.logger.info(`user: ${JSON.stringify(user)}`);
      functions.logger.info(`company: ${JSON.stringify(company)}`);
      if(user && company){
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

      functions.logger.info("SENDING employee");

      response.status(200).send({data:employee})
      }else{
        functions.logger.error("The user or the company is missing");
        response.status(500).send({error: {"code": 501, "message": "The user or the company is missing"} })
      }
    }else{
      functions.logger.error("The user is already employed at ${MAX_NUMBER_OF_EMPLOYERS} companies");
      response.status(500).send({
        error: {
          "code": 501,
          "message": `The user is already employed at ${MAX_NUMBER_OF_EMPLOYERS} companies`
        }
      })
    }
  }catch(err){
    functions.logger.error(err);
    response.status(500).send({error : {"code": 501,"message": err.message}})
  }

});

// export const getUsers = functions.https.onRequest((request, response) => {
//     admin.firestore().collection('users').get()
//     .then(snapshot =>{
//         const data  = snapshot.docs
//         response.send(data)
//     })
//      .catch(error =>{
//         functions.logger.error(error)
//         response.status(500).send(error)
//     })
// });

// export const onUserCreate = functions.firestore.document('/users/{userId}').onCreate((snapshot, context) => {
//     context.params.userId
// });
  

// export const onUserUpdate = functions.firestore.document('/users/{userId}').onUpdate(change => {
//     const before = change.before.data()
//     const after = change.after.data()
//     if(before!=after){
//       if(before.firstName != after.firstName){

//       }

//       //return Promise
//     }else{
//       return null
//     }
//   });

// exports.scheduledFunction = functions.pubsub.schedule('* * * * *').onRun(async (context) => {
//   await admin.firestore().collection('timers').doc('timer1').update({"time": admin.firestore.Timestamp.now()});
//   return console.log('success')
// });
  
