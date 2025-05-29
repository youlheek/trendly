import React, { useState, useRef } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Card, CardContent } from "@/components/ui/card";
import { Client } from "@stomp/stompjs";

export default function StompWebSocketTester() {
    const [url, setUrl] = useState("wss://your-server/ws");
    const [channel, setChannel] = useState("/sub/chat");
    const [sendDest, setSendDest] = useState("/pub/chat");
    const [token, setToken] = useState("");
    const [message, setMessage] = useState("");
    const [log, setLog] = useState("");
    const clientRef = useRef(null);

    const appendLog = (msg) => {
        setLog((prev) => prev + msg + "\n");
    };

    const connect = () => {
        const client = new Client({
            brokerURL: url,
            connectHeaders: {
                Authorization: token
            },
            onConnect: () => {
                appendLog("✅ Connected to " + url);
                client.subscribe(channel, (message) => {
                    appendLog("📩 Received: " + message.body);
                });
                appendLog("📡 Subscribed to " + channel);
            },
            onStompError: (frame) => {
                appendLog("❌ STOMP error: " + frame.body);
            },
            onWebSocketError: (e) => {
                appendLog("❌ WebSocket error: " + e);
            },
        });
        client.activate();
        clientRef.current = client;
    };

    const sendMessage = () => {
        if (clientRef.current && clientRef.current.connected) {
            clientRef.current.publish({
                destination: sendDest,
                body: message,
            });
            appendLog("📤 Sent: " + message);
            setMessage("");
        } else {
            appendLog("⚠️ Not connected.");
        }
    };

    return (
        <div className="p-4 grid gap-4 max-w-2xl mx-auto">
            <Card>
                <CardContent className="grid gap-2 p-4">
                    <Input value={url} onChange={(e) => setUrl(e.target.value)} placeholder="WebSocket URL (wss://...)" />
                    <Input value={channel} onChange={(e) => setChannel(e.target.value)} placeholder="Subscribe Topic (/sub/...)" />
                    <Input value={sendDest} onChange={(e) => setSendDest(e.target.value)} placeholder="Send Destination (/pub/...)" />
                    <Input value={token} onChange={(e) => setToken(e.target.value)} placeholder="Authorization Token (Bearer ...)" />
                    <Textarea value={message} onChange={(e) => setMessage(e.target.value)} placeholder="Message to send..." />
                    <div className="flex gap-2">
                        <Button onClick={connect}>Connect & Subscribe</Button>
                        <Button onClick={sendMessage}>Send Message</Button>
                    </div>
                </CardContent>
            </Card>

            <Card>
                <CardContent className="p-4 h-64 overflow-auto bg-black text-white text-sm whitespace-pre-wrap">
                    {log || "Logs will appear here..."}
                </CardContent>
            </Card>
        </div>
    );
}
