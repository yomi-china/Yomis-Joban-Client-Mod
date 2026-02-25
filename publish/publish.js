const { EmbedBuilder, WebhookClient } = require('discord.js')
const { mwn } = require("mwn");
const { readFileSync } = require('fs');
const JCM_ROLE = "<@&944884315853045764>";
let bot;

let args = process.argv.slice(2);
if(args.length < 4) {
	console.log("Not enough argument! <JCM Version> <Discord Webhook ID:Token> <Mediawiki Username:Password> <Path to changelog>")
    return;
}

let version = args[0].substring(1);
let authDCId = args[1].split(":")[0];
let authDCToken = args[1].split(":")[1];
let authMWUSR = args[2].split(":")[0]
let authMWPASSWD = args[2].split(":")[1];
let path = args[3];
let lines = readFileSync(path, 'utf-8').split(/\r?\n/);

if(version.length < 5) {
    console.log("Version length must be X.X.X");
    return;
}

if(authDCId.length > 0 && authDCToken.length > 0) {
    let fieldTitle = "";
    let fieldDesc = "";

    let embed = new EmbedBuilder();

    for (let i = 0; i < lines.length; i++) {
        let line = lines[i];
        if(line.length > 0) {
            if (line.startsWith("# ")) {
                embed.setTitle(line.substring(2));
                continue;
            }

            if (line.startsWith("## ")) {
                if(!fieldDesc.length == 0) {
                    if(fieldTitle.length == 0) {
                        embed.setDescription(fieldDesc);
                    } else {
                        embed.addFields({ name: fieldTitle, value: fieldDesc, inline: false });
                    }
                }

                fieldTitle = line.substring(3);
                fieldDesc = "";
            } else if(line.startsWith("![]")) {
                embed.setImage(line.split("![]")[1].replace("(", "").replace(")", ""));
            } else {
                fieldDesc += line + "\n";
            }
        }
		
		if (i == lines.length - 1) {
            embed.addFields({ name: fieldTitle, value: fieldDesc, inline: false });
        }
    }

    const webhookClient = new WebhookClient({ id: authDCId, token: authDCToken });
    webhookClient.send({ content: JCM_ROLE, embeds: [embed] });
}

if (authMWUSR != "null" && authMWPASSWD != "null") {
    (async () => {
        for(let i = 0; i < lines.length; i++) {
            let line = lines[i].trim();

            if(line.startsWith("# ")) {
                if(line.toLowerCase().includes("has been released")) {
                    //Don't show "JCM XXX has been released" in the wiki
                    lines[i] = "";
                } else {
                    //Convert to MW Header
                    lines[i] = `= ${line.substring(2)} =`
                }
            }

            if(line.startsWith("## ")) {
                //Convert to MW Syntax
                lines[i] = `== ${line.substring(2)} ==`
            }

            if(line.trim().startsWith("-")) {
                lines[i] = line.replace("-", "*");
            }

            //External Download Link
            if(/^\[.+]\(.+\)/.test(line)) {
                let name = line.match(/\[.+]/)[0].replaceAll(/\[|\]/ig, "");
                let url = line.match(/\(.+\)/)[0].replaceAll(/\(|\)/ig, "");

                lines[i] = line.replace(/^\[.+]\(.+\)/, `[${url} ${name}]`) + "\n";
            }

            // Image, too lazy to upload
            if(line.startsWith("![]")) {
                lines[i] = "";
            }
        }

        console.log("Logging in to MediaWiki")
		try {
			bot = await mwn.init({
				apiUrl: 'https://www.joban.tk/w/api.php',
	
				username: authMWUSR,
				password: authMWPASSWD,
				userAgent: 'JCM',
				defaultParams: {
					assert: 'user'
				}
			});
	
			await bot.create("JCM:" + version, lines.join("\n"), "")
	
			bot.edit("JCM:Changelogs", (rev) => {
				let text = rev.content.replace(" {{Tag|2BC643|latest}}", "");
				let newText = "";
	
				for(let line of text.split(/[\r\n]+/)) {
					newText += line + "\n";
					if(line.includes("!Tags")) {
						newText += "\n|-\n"
						newText += `|[[JCM:${version}|v${version}]]\n`
						newText += `|{{Tag|2BC643|supported}} {{Tag|2BC643|latest}}\n`
					}
				}
				
				return {
					text: newText,
					summary: "Bump release",
					minor: false
				}
			})
			console.log("Finished updating wiki.")
		} catch (e) {
			console.log("Failed to update the wiki.")
			console.log(e)
		}

    })();
}
