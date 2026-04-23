import http from 'http';

function post(data) {
    return new Promise((resolve, reject) => {
        const options = {
            hostname: 'localhost',
            port: 3000,
            path: '/scan',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Content-Length': data.length
            }
        };

        const req = http.request(options, (res) => {
            let body = '';
            res.on('data', (chunk) => body += chunk);
            res.on('end', () => resolve(JSON.parse(body)));
        });

        req.on('error', (e) => reject(e));
        req.write(data);
        req.end();
    });
}

async function test() {
    try {
        console.log("1. Scan Unit 1 RAM with ABC...");
        const res1 = await post(JSON.stringify({
            project_id: "test", date: "2024-01-01", technician_id: "T1",
            unit: 1, part: "RAM", barcode: "ABC"
        }));
        console.log("Res1:", res1);

        console.log("2. Scan Unit 1 RAM with DEF (Slot Duplicate)...");
        const res2 = await post(JSON.stringify({
            project_id: "test", date: "2024-01-01", technician_id: "T1",
            unit: 1, part: "RAM", barcode: "DEF"
        }));
        console.log("Res2:", res2);

        console.log("3. Scan Unit 2 SSD with ABC (Global Duplicate)...");
        const res3 = await post(JSON.stringify({
            project_id: "test", date: "2024-01-01", technician_id: "T1",
            unit: 2, part: "SSD", barcode: "ABC"
        }));
        console.log("Res3:", res3);

    } catch (e) {
        console.error(e);
    }
}

test();
