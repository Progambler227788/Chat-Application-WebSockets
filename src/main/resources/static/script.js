let stompClient = null;
let username = null;

// DOM Elements
const connectBtn = document.getElementById('connectBtn');
const disconnectBtn = document.getElementById('disconnectBtn');
const sendBtn = document.getElementById('sendBtn');
const typingBtn = document.getElementById('typingBtn');
const messageInput = document.getElementById('messageInput');
const usernameInput = document.getElementById('username');
const messagesDiv = document.getElementById('messages');
const connectionStatusDiv = document.getElementById('connectionStatus');
const onlineUsersDiv = document.getElementById('onlineUsers');
const activityNotificationsDiv = document.getElementById('activityNotifications');

// Connect to WebSocket
// Updated connect function
function connect() {
    username = usernameInput.value.trim();
    if (!username) {
        alert('Please enter a username');
        return;
    }

    const socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);
    stompClient.debug = null; // Disable STOMP logging

    stompClient.connect({},
        function(frame) {
            // Connection established
            connectionStatusDiv.textContent = `Connected as ${username}`;
            connectBtn.disabled = true;
            disconnectBtn.disabled = false;
            sendBtn.disabled = false;
            typingBtn.disabled = false;
            messageInput.disabled = false;

            // Subscriptions
            stompClient.subscribe('/topic/public', (message) => {
                displayMessage(JSON.parse(message.body));
            });

            stompClient.subscribe('/topic/activity', (notification) => {
                displayActivity(JSON.parse(notification.body));
            });

            stompClient.subscribe('/topic/presence', (presence) => {
                displayPresence(JSON.parse(presence.body));
            });

            stompClient.subscribe('/user/queue/errors', (error) => {
                displayError(error.body);
            });

            // Send join notification
            stompClient.send("/app/chat.join", {},
                JSON.stringify({username: username, sessionId: 'web-'+Date.now()})
            );
        },
        function(error) {
            console.error('Connection error', error);
            connectionStatusDiv.textContent = "Connection failed - retrying...";
            setTimeout(connect, 5000);
        }
    );
}

// Disconnect from WebSocket
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    connectionStatusDiv.textContent = "Disconnected";
    connectBtn.disabled = false;
    disconnectBtn.disabled = true;
    sendBtn.disabled = true;
    typingBtn.disabled = true;
    messageInput.disabled = true;
    onlineUsersDiv.innerHTML = '';
    activityNotificationsDiv.innerHTML = '';
}

// Send a chat message
function sendMessage() {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const message = {
            roomId: "test-room",
            sender: username,
            content: messageContent,
            type: "CHAT"
        };
        stompClient.send("/app/chat.send", {}, JSON.stringify(message));
        messageInput.value = '';
    }
}

// Send typing notification
function sendTyping() {
    if (stompClient) {
        const typingNotification = {
            username: username,
            conversationId: "test-room"
        };
        stompClient.send("/app/chat.typing", {}, JSON.stringify(typingNotification));
    }
}

// Display incoming message
function displayMessage(message) {
    const messageElement = document.createElement('div');
    messageElement.classList.add('message');

    if (message.sender === username) {
        messageElement.classList.add('sent');
    }

    messageElement.innerHTML = `
        <strong>${message.sender}</strong>: ${message.content}
        <div class="message-time">${new Date().toLocaleTimeString()}</div>
    `;
    messagesDiv.appendChild(messageElement);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

// Display activity notification
function displayActivity(activity) {
    const notificationElement = document.createElement('div');
    notificationElement.classList.add('notification');

    if (activity.activityType === "TYPING") {
        notificationElement.classList.add('typing-notification');
        notificationElement.textContent = `${activity.username} is typing...`;
    } else if (activity.activityType === "JOINED") {
        notificationElement.textContent = `${activity.username} joined the chat`;
    } else if (activity.activityType === "LEFT") {
        notificationElement.textContent = `${activity.username} left the chat`;
    }

    activityNotificationsDiv.appendChild(notificationElement);
    activityNotificationsDiv.scrollTop = activityNotificationsDiv.scrollHeight;
}

// Display presence update
function displayPresence(update) {
    // This would be more sophisticated in a real app
    const userElement = document.createElement('div');
    userElement.classList.add('user-status');
    userElement.textContent = update.username;
    onlineUsersDiv.appendChild(userElement);
}

// Display error
function displayError(error) {
    const errorElement = document.createElement('div');
    errorElement.classList.add('notification');
    errorElement.style.color = 'red';
    errorElement.textContent = `Error: ${error}`;
    activityNotificationsDiv.appendChild(errorElement);
}

// Event Listeners
connectBtn.addEventListener('click', connect);
disconnectBtn.addEventListener('click', disconnect);
sendBtn.addEventListener('click', sendMessage);
typingBtn.addEventListener('click', sendTyping);

// Send message on Enter key
messageInput.addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        sendMessage();
    }
});